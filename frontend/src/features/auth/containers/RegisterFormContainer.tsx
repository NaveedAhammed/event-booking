import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { zodResolver } from "@hookform/resolvers/zod";
import authService from "../services/authService";
import RegisterForm from "../components/RegisterForm";
import { registerSchema, type RegisterSchema } from "../schema/registerSchema";

const RegisterFormContainer = () => {
	const navigate = useNavigate();
	const {
		handleSubmit,
		register,
		formState: { errors, isSubmitting },
		watch,
		setValue,
	} = useForm<RegisterSchema>({
		resolver: zodResolver(registerSchema),
	});

	const onSubmit = async (data: RegisterSchema) => {
		try {
			const res = await authService.register(data);
			console.log("Registered: ", res);

			navigate("/");
		} catch (err) {
			console.log("Register error:", err);
		}
	};

	return (
		<div className="flex items-center justify-center py-2">
			<RegisterForm
				onSubmit={handleSubmit(onSubmit)}
				register={register}
				errors={errors}
				isSubmitting={isSubmitting}
				watch={watch}
				setValue={setValue}
			/>
		</div>
	);
};

export default RegisterFormContainer;
