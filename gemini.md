# React TypeScript Project Rules (`gemini.md`)

## 1. Core Principles

* **Functional Focus:** Use Functional Components and Hooks exclusively.
* **Type Safety:** Aim for total type coverage. Avoid `any`.
* **Consistency:** Maintain a uniform structure for components and state.

---

## 2. Component Architecture

* **Folder Structure:** 
  * `src/components/ui`: Common Bootstrap wrappers (Custom Buttons, Modals).
  * `src/components/features`: Feature-specific logic.
* **File Naming:** PascalCase for components (`UserCard.tsx`).
* **Bootstrap Integration:** Use `react-bootstrap` for complex components (Modals, Dropdowns) to ensure accessibility and React lifecycle compatibility.

---

## 3. Styling Standards (Bootstrap 5)

* **Utility First:** Use Bootstrap utility classes (e.g., `d-flex`, `mt-3`, `text-primary`) for layout and spacing.
* **Semantic Customization:** Override Bootstrap variables in a `custom.scss` file rather than using `!important` in CSS.
* **Conditional Classes:** Use the `classnames` library for clean conditional logic:
```tsx
import classNames from 'classnames';

const statusClasses = classNames('btn', {
  'btn-success': isOnline,
  'btn-danger': !isOnline,
  'disabled': isLoading
});

```


* **Layout:** Always use the Bootstrap Grid system (`Container`, `Row`, `Col`) for responsive design.

---

## 4. Data Fetching (TanStack Query)

* **Query Keys:** Use a centralized factory/object for query keys.
* **Custom Hooks:** Wrap all `useQuery` and `useMutation` calls. Never call them directly in the UI component.

```tsx
export const useUser = (userId: string) => {
  return useQuery({
    queryKey: ['users', userId],
    queryFn: () => fetchUserById(userId),
    staleTime: 1000 * 60 * 5,
  });
};

```

---

## 5. Form Management (React Hook Form)

* **Schema Validation:** Use **Zod** for schema definitions.
* **Bootstrap Integration:** Use the `is-invalid` class and `Form.Control.Feedback` for displaying errors.

```tsx
import { Form, Button } from 'react-bootstrap';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';

const schema = z.object({
  email: z.string().email('Invalid email address'),
});

type FormFields = z.infer<typeof schema>;

export const LoginForm = () => {
  const { register, handleSubmit, formState: { errors } } = useForm<FormFields>({
    resolver: zodResolver(schema),
  });

  return (
    <Form onSubmit={handleSubmit((data) => console.log(data))}>
      <Form.Group className="mb-3">
        <Form.Label>Email</Form.Label>
        <Form.Control 
          {...register('email')} 
          isInvalid={!!errors.email} 
        />
        <Form.Control.Feedback type="invalid">
          {errors.email?.message}
        </Form.Control.Feedback>
      </Form.Group>
      <Button type="submit">Login</Button>
    </Form>
  );
};

```

---

## 6. Performance & Rendering

* **Lists:** Use stable keys (ID) for array mapping.
* **Memoization:** Use `useMemo` for expensive filtering/sorting of Bootstrap table data.

---

## 7. Git & Workflow

* **Commits:** Conventional Commits (`feat:`, `fix:`, etc.).
* **Quality:** All code must pass `eslint` and `tsc` (TypeScript Compiler) check before PR approval.

---

## 8. Testing Support

* **Selectors:** Use `data-testid` attributes for elements targeted by Selenium/E2E tests. Avoid using CSS classes for test selectors as they may change.
