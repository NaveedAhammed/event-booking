import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { type LoginSchema, loginSchema } from "../schema/loginSchema";
import { zodResolver } from "@hookform/resolvers/zod";
import LoginForm from "../components/LoginForm";

const LoginFormContainer = () => {
	const navigate = useNavigate();
	const {
		handleSubmit,
		register,
		formState: { errors, isSubmitting },
	} = useForm<LoginSchema>({
		resolver: zodResolver(loginSchema),
	});

	const onSubmit = async (data: LoginSchema) => {
		try {
			const res = await fetch("http://localhost:4000/api/auth/login", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(data),
			});

			const result = await res.json();

			if (!res.ok) {
				alert(result.message);
				return;
			}

			navigate("/");
		} catch (err) {
			console.log("Login error:", err);
		}
	};

	return (
		<div className="h-full flex items-center justify-center bg-amber-100">
			<LoginForm
				onSubmit={handleSubmit(onSubmit)}
				register={register}
				errors={errors}
				isSubmitting={isSubmitting}
			/>
		</div>
	);
};

export default LoginFormContainer;
