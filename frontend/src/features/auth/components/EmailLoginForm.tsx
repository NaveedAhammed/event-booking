import type { UseFormRegister, FieldErrors } from "react-hook-form";
import type { EmailLoginSchema } from "../schema/loginSchema";
import InputField from "@/components/Input/Input";
import Button from "@/components/Button/Button";
import { LuLockKeyhole, LuMail } from "react-icons/lu";
import { HiOutlineEyeOff } from "react-icons/hi";
import Heading from "@/components/Heading/Heading";

interface Props {
	onSubmit: () => void;
	register: UseFormRegister<EmailLoginSchema>;
	errors: FieldErrors<EmailLoginSchema>;
	isSubmitting: boolean;
}

function EmailLoginForm({ onSubmit, register, errors, isSubmitting }: Props) {
	return (
		<form onSubmit={onSubmit} className="bg-white w-96 flex flex-col gap-4">
			<Heading
				title="Welcome Back!"
				subTitle="Login to your account to continue booking events and managing your
				favorites."
			/>

			<InputField
				leadingIcon={LuMail}
				type="email"
				placeholder="Email"
				registration={register("email")}
				error={errors.email}
				disabled={isSubmitting}
			/>

			<InputField
				leadingIcon={LuLockKeyhole}
				type="password"
				registration={register("password")}
				error={errors.password}
				placeholder="Password"
				trailingIcon={HiOutlineEyeOff}
				disabled={isSubmitting}
			/>

			<Button type="submit" disabled={isSubmitting}>
				Login
			</Button>
		</form>
	);
}

export default EmailLoginForm;
