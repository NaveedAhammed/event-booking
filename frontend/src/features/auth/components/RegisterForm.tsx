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
			className="bg-white p-6 w-full max-w-md flex flex-col gap-4"
		>
			<h2 className="text-2xl font-semibold text-center">Register</h2>

			<InputField
				type="text"
				label="Name"
				registration={register("name")}
				error={errors.name}
			/>

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

			<Select
				label="Role"
				name="role"
				value={watch("role")}
				onChange={(val) => setValue("role", val)}
				options={options}
				error={errors.role}
			/>

			<Button type="submit" disabled={isSubmitting}>
				Register
			</Button>
		</form>
	);
};

export default RegisterForm;
