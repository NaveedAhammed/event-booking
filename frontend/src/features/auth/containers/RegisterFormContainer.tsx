import { v4 as uuid } from "uuid";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { zodResolver } from "@hookform/resolvers/zod";
import authService from "../services/authService";
import RegisterForm from "../components/RegisterForm";
import { registerSchema, type RegisterSchema } from "../schema/registerSchema";
import Button from "@/components/Button/Button";
import { FcGoogle } from "react-icons/fc";
import { FaGithub } from "react-icons/fa";

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

	const googleOAuthHandler = () => {
		const redirectUri = encodeURIComponent(
			"http://localhost:4000/api/auth/oauth/google"
		);
		const clientId = import.meta.env.VITE_GOOGLE_CLIENT_ID;
		const scope = encodeURIComponent("openid email profile");
		const state = uuid();

		window.location.href = `https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=${clientId}&redirect_uri=${redirectUri}&scope=${scope}&state=${state}&prompt=select_account`;
	};

	return (
		<div className="py-10 flex flex-col">
			<RegisterForm
				onSubmit={handleSubmit(onSubmit)}
				register={register}
				errors={errors}
				isSubmitting={isSubmitting}
				watch={watch}
				setValue={setValue}
			/>

			<div className="border-t border-gray-200 mt-6 relative">
				<span className="text-gray-400 text-xs absolute left-[50%] top-[50%] translate-x-[-50%] translate-y-[-55%] bg-white px-2">
					or you can sign in with
				</span>
			</div>

			<Button
				variant="outline"
				className="gap-2 text-sm mt-6"
				onClick={googleOAuthHandler}
			>
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

export default RegisterFormContainer;
