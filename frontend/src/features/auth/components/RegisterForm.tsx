import type {
	UseFormRegister,
	FieldErrors,
	UseFormWatch,
	UseFormSetValue,
} from "react-hook-form";
import InputField from "@/components/Input/Input";
import Button from "@/components/Button/Button";
import type { RegisterSchema } from "../schema/registerSchema";
import { FiUser } from "react-icons/fi";
import { LuLockKeyhole, LuMail } from "react-icons/lu";
import { HiOutlineEyeOff } from "react-icons/hi";

interface Props {
	onSubmit: () => void;
	register: UseFormRegister<RegisterSchema>;
	errors: FieldErrors<RegisterSchema>;
	isSubmitting: boolean;
	watch: UseFormWatch<RegisterSchema>;
	setValue: UseFormSetValue<RegisterSchema>;
}

const RegisterForm = ({ onSubmit, register, errors, isSubmitting }: Props) => {
	return (
		<form onSubmit={onSubmit} className="bg-white w-96 flex flex-col gap-4">
			<h2 className="text-2xl font-semibold">Create an account</h2>

			<p className="text-sm text-gray-500 mb-4">
				Create an account so you can save event for your favorites, and checkout
				faster.
			</p>

			<InputField
				leadingIcon={FiUser}
				type="text"
				registration={register("name")}
				error={errors.name}
				placeholder="Username"
			/>

			<InputField
				leadingIcon={LuMail}
				type="email"
				registration={register("email")}
				error={errors.email}
				placeholder="Email"
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
				Sign Up
			</Button>
		</form>
	);
};

export default RegisterForm;
