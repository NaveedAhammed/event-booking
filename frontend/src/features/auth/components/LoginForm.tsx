import type { UseFormRegister, FieldErrors } from "react-hook-form";
import type { LoginSchema } from "../schema/loginSchema";
import InputField from "@/components/Input/Input";
import Button from "@/components/Button/Button";

interface Props {
	onSubmit: () => void;
	register: UseFormRegister<LoginSchema>;
	errors: FieldErrors<LoginSchema>;
	isSubmitting: boolean;
}

const LoginForm = ({ onSubmit, register, errors, isSubmitting }: Props) => {
	return (
		<form
			onSubmit={onSubmit}
			className="bg-white p-6 rounded shadow-md w-full max-w-md flex flex-col gap-4"
		>
			<h2 className="text-2xl font-semibold text-center">Login</h2>

			<InputField
				type="email"
				label="Email"
				registration={register("email")}
				error={errors.email}
			/>

			<InputField
				type="password"
				label="Password"
				registration={register("password")}
				error={errors.password}
			/>

			<Button type="submit" disabled={isSubmitting}>
				Login
			</Button>
		</form>
	);
};

export default LoginForm;
