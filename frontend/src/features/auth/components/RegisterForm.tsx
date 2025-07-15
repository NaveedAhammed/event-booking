import type { UseFormRegister, FieldErrors } from "react-hook-form";
import InputField from "@/components/Input/Input";
import Button from "@/components/Button/Button";
import type { RegisterSchema } from "../schema/registerSchema";
import { FiUser } from "react-icons/fi";
import { LuLockKeyhole, LuMail } from "react-icons/lu";
import { HiOutlineEyeOff } from "react-icons/hi";
import Heading from "@/components/Heading/Heading";

interface Props {
	onSubmit: () => void;
	register: UseFormRegister<RegisterSchema>;
	errors: FieldErrors<RegisterSchema>;
	isSubmitting: boolean;
}

const RegisterForm = ({ onSubmit, register, errors, isSubmitting }: Props) => {
	return (
		<form onSubmit={onSubmit} className="bg-white w-96 flex flex-col gap-4">
			<Heading
				title="Create an account"
				subTitle="Create an account so you can save event for your favorites, and
				checkout faster."
			/>

			<InputField
				leadingIcon={FiUser}
				type="text"
				registration={register("name")}
				error={errors.name}
				placeholder="Username"
				disabled={isSubmitting}
			/>

			<InputField
				leadingIcon={LuMail}
				type="email"
				registration={register("email")}
				error={errors.email}
				placeholder="Email"
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

			<input type="hidden" {...register("role", { value: "USER" })} />

			<Button type="submit" disabled={isSubmitting}>
				Sign Up
			</Button>
		</form>
	);
};

export default RegisterForm;
