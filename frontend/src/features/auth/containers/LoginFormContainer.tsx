import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { type LoginSchema, loginSchema } from "../schema/loginSchema";
import { zodResolver } from "@hookform/resolvers/zod";
import LoginForm from "../components/LoginForm";
import authService from "../services/authService";
import Button from "@/components/Button/Button";
import { FcGoogle } from "react-icons/fc";
import { FaGithub } from "react-icons/fa";

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
			const res = await authService.login(data);
			console.log("Logged in: ", res);

			navigate("/");
		} catch (err) {
			console.log("Login error:", err);
		}
	};

	return (
		<div className="py-10 flex flex-col">
			<LoginForm
				onSubmit={handleSubmit(onSubmit)}
				register={register}
				errors={errors}
				isSubmitting={isSubmitting}
			/>

			<div className="border-t border-gray-200 mt-6 relative">
				<span className="text-gray-400 text-xs absolute left-[50%] top-[50%] translate-x-[-50%] translate-y-[-55%] bg-white px-2">
					or you can sign in with
				</span>
			</div>

			<Button variant="outline" className="gap-2 text-sm mt-6">
				<FcGoogle size={20} />
				<span>Continue with Google</span>
			</Button>

			<Button variant="outline" className="gap-2 text-sm mt-4">
				<FaGithub size={20} />
				<span>Continue with GitHub</span>
			</Button>
		</div>
	);
};

export default LoginFormContainer;
