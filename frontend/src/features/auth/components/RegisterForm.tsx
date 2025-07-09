import type {
	UseFormRegister,
	FieldErrors,
	UseFormWatch,
	UseFormSetValue,
} from "react-hook-form";
import InputField from "@/components/Input/Input";
import Button from "@/components/Button/Button";
import type { RegisterSchema } from "../schema/registerSchema";
import { Select } from "@/components/Select/Select";
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

const options = [
	{ label: "User", value: "USER" },
	{ label: "Organizer", value: "ORGANIZER" },
	{ label: "Admin", value: "ADMIN" },
];

const RegisterForm = ({
	onSubmit,
	register,
	errors,
	isSubmitting,
	watch,
	setValue,
}: Props) => {
	return (
		<form
			onSubmit={onSubmit}
			className="bg-white p-6 w-96 flex flex-col gap-4"
		>
			<h2 className="text-2xl font-semibold">Create an account</h2>

			<p className="text-sm text-gray-500 mb-4">
				Create an account so you can save event for your favorites, and
				checkout faster.
			</p>

			<InputField
				leadingIcon={FiUser}
				type="text"
				label="Name"
				registration={register("name")}
				error={errors.name}
				placeholder="Username"
			/>

			<InputField
				leadingIcon={LuMail}
				type="email"
				label="Email"
				registration={register("email")}
				error={errors.email}
				placeholder="Email"
			/>

			<InputField
				leadingIcon={LuLockKeyhole}
				type="password"
				label="Password"
				registration={register("password")}
				error={errors.password}
				placeholder="Password"
				tralingIcon={HiOutlineEyeOff}
			/>

			<Select
				label="Role"
				name="role"
				value={watch("role")}
				onChange={(val) => setValue("role", val)}
				options={options}
				error={errors.role}
			/>

			<Button type="submit" disabled={isSubmitting}>
				Sign Up
			</Button>
		</form>
	);
};

export default RegisterForm;
