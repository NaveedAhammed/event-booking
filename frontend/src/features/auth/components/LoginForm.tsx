import type { UseFormRegister, FieldErrors } from "react-hook-form";
import type { LoginSchema } from "../schema/loginSchema";
import InputField from "@/components/Input/Input";
import Button from "@/components/Button/Button";
import { LuLockKeyhole, LuMail } from "react-icons/lu";
import { HiOutlineEyeOff } from "react-icons/hi";

interface Props {
	onSubmit: () => void;
	register: UseFormRegister<LoginSchema>;
	errors: FieldErrors<LoginSchema>;
	isSubmitting: boolean;
}

const LoginForm = ({ onSubmit, register, errors, isSubmitting }: Props) => {
	return (
		<form onSubmit={onSubmit} className="bg-white p-6 w-96 flex flex-col gap-4">
			<h2 className="text-2xl font-semibold">Welcome Back!</h2>

			<p className="text-sm text-gray-500 mb-4">
				Login to your account to continue booking events and managing your
				favorites.
			</p>

			<InputField
				leadingIcon={LuMail}
				type="email"
				placeholder="Email"
				registration={register("email")}
				error={errors.email}
			/>

			<InputField
				leadingIcon={LuLockKeyhole}
				type="password"
				registration={register("password")}
				error={errors.password}
				placeholder="Password"
				trailingIcon={HiOutlineEyeOff}
			/>

			<Button type="submit" disabled={isSubmitting}>
				Login
			</Button>
		</form>
	);
};

export default LoginForm;
