import React from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Form, Button, Card, ListGroup, Spinner, Alert } from 'react-bootstrap';
import classNames from 'classnames';

interface DiaryEntry {
    id: number;
    title: string;
    content: string;
    createdAt: string;
}

interface DiaryEntriesProps {
    token: string;
}

const entrySchema = z.object({
    title: z.string().min(1, "Title is required"),
    content: z.string().min(1, "Content is required"),
});

type EntryFormFields = z.infer<typeof entrySchema>;

const DiaryEntries: React.FC<DiaryEntriesProps> = ({ token }) => {
    const queryClient = useQueryClient();
    const { register, handleSubmit, reset, formState: { errors } } = useForm<EntryFormFields>({
        resolver: zodResolver(entrySchema)
    });

    const fetchEntries = async (): Promise<DiaryEntry[]> => {
        const response = await fetch('http://localhost:8080/api/entries', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    };

    const { data: entries, isLoading, isError } = useQuery({
        queryKey: ['entries'],
        queryFn: fetchEntries
    });

    const createEntryMutation = useMutation({
        mutationFn: async (newEntry: EntryFormFields) => {
            const response = await fetch('http://localhost:8080/api/entries', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(newEntry)
            });
            if (!response.ok) {
                throw new Error('Failed to create entry');
            }
            return response.json();
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['entries'] });
            reset();
        }
    });

    const deleteEntryMutation = useMutation({
        mutationFn: async (id: number) => {
            const response = await fetch(`http://localhost:8080/api/entries/${id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            if (!response.ok) {
                throw new Error('Failed to delete entry');
            }
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['entries'] });
        }
    });

    const onSubmit = (data: EntryFormFields) => {
        createEntryMutation.mutate(data);
    };

    if (isLoading) return <Spinner animation="border" />;
    if (isError) return <Alert variant="danger">Error loading entries</Alert>;

    return (
        <div>
            <Card className="mb-4">
                <Card.Body>
                    <Card.Title>New Entry</Card.Title>
                    <Form onSubmit={handleSubmit(onSubmit)}>
                        <Form.Group className="mb-3">
                            <Form.Control
                                type="text"
                                placeholder="Title"
                                {...register('title')}
                                isInvalid={!!errors.title}
                            />
                            <Form.Control.Feedback type="invalid">
                                {errors.title?.message}
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Control
                                as="textarea"
                                rows={3}
                                placeholder="Content"
                                {...register('content')}
                                isInvalid={!!errors.content}
                            />
                            <Form.Control.Feedback type="invalid">
                                {errors.content?.message}
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Button
                            type="submit"
                            variant="success"
                            disabled={createEntryMutation.isPending}
                        >
                            {createEntryMutation.isPending ? 'Adding...' : 'Add Entry'}
                        </Button>
                    </Form>
                </Card.Body>
            </Card>

            <ListGroup>
                {entries?.map(entry => (
                    <ListGroup.Item key={entry.id} action className="d-flex w-100 justify-content-between align-items-start">
                        <div className="ms-2 me-auto">
                            <div className="fw-bold">{entry.title}</div>
                            {entry.content}
                            <div className="text-muted small mt-1">
                                {new Date(entry.createdAt).toLocaleDateString()}
                            </div>
                        </div>
                        <Button
                            variant="danger"
                            size="sm"
                            onClick={() => deleteEntryMutation.mutate(entry.id)}
                            className={classNames({ 'disabled': deleteEntryMutation.isPending })}
                        >
                            Delete
                        </Button>
                    </ListGroup.Item>
                ))}
            </ListGroup>
        </div>
    );
}

export default DiaryEntries;
