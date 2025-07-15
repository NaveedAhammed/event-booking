import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import Button from "@/components/Button/Button";
import { FcGoogle } from "react-icons/fc";
import { FaGithub } from "react-icons/fa";
import {
	mobileLoginSchema,
	type MobileLoginSchema,
} from "../schema/loginSchema";
import { LuMail } from "react-icons/lu";
import MobileLoginForm from "../components/MobileLoginForm";
import { useEffect, useReducer } from "react";
import authService from "../services/authService";
import { useNavigate } from "react-router-dom";
import { AxiosError } from "axios";
import toast from "react-hot-toast";

type Action =
	| { type: "sendOtpStart" }
	| { type: "sendOtpSuccess" }
	| { type: "sendOtpError"; payload: string }
	| { type: "otpVerifyStart" }
	| { type: "otpVerifySuccess" }
	| { type: "otpVerifyError"; payload: string }
	| { type: "decrementTimer" }
	| { type: "reset" }
	| { type: "updateMobile"; payload: string }
	| { type: "updateOtp"; payload: string }
	| { type: "resendCode" };

interface State {
	showOtpComponent: boolean;
	timer: number;
	errorMessage?: string;
}

const initialState: State = {
	showOtpComponent: false,
	timer: 60,
	errorMessage: undefined,
};

const reducer = (state: State, action: Action): State => {
	switch (action.type) {
		case "sendOtpSuccess":
			return {
				...state,
				showOtpComponent: true,
				timer: 60,
			};
		case "decrementTimer":
			return { ...state, timer: state.timer - 1 };
		case "resendCode":
			return { ...state, timer: 60 };
		case "reset":
			return initialState;
		default:
			return state;
	}
};

const OTP_LENGTH = 6;

const MobileLoginFormContainer = () => {
	const [state, dispatch] = useReducer(reducer, initialState);

	const navigate = useNavigate();

	const { showOtpComponent, timer } = state;

	const {
		handleSubmit,
		register,
		setError,
		formState: { errors, isSubmitting },
	} = useForm<MobileLoginSchema>({
		resolver: zodResolver(mobileLoginSchema),
	});

	const onSubmit = async (data: MobileLoginSchema) => {
		if (!showOtpComponent) return getOtp(data.mobile!);
		else if (showOtpComponent) return verifyOtp(data);
	};

	const getOtp = async (mobile: string) => {
		try {
			await authService.getOtp(mobile);
			dispatch({ type: "sendOtpSuccess" });
		} catch (error) {
			if (error instanceof AxiosError) {
				toast.error(error.message);
			}
		}
	};

	const verifyOtp = async (data: MobileLoginSchema) => {
		if (showOtpComponent && !data.otp) {
			setError("otp", { type: "manual", message: "Otp is required" });
			return;
		}

		if (data.otp && data.otp.trim().length < OTP_LENGTH) {
			setError("otp", {
				type: "manual",
				message: `Otp must be ${OTP_LENGTH} digits`,
			});
			return;
		}

		try {
			await authService.verifyOtp(data);
		} catch (error) {
			if (error instanceof AxiosError) {
				toast.error(error.message);
			}
		}
	};

	const requestNewCodeHandler = () => {
		dispatch({ type: "resendCode" });
	};

	useEffect(() => {
		let interval: NodeJS.Timeout;
		if (showOtpComponent && timer > 0) {
			interval = setInterval(() => {
				dispatch({ type: "decrementTimer" });
			}, 1000);
		}
		return () => clearInterval(interval);
	}, [showOtpComponent, timer]);

	return (
		<div className="py-10 flex flex-col">
			<MobileLoginForm
				register={register}
				errors={errors}
				isSubmitting={isSubmitting}
				onSubmit={handleSubmit(onSubmit)}
				showOtpComponent={showOtpComponent}
				timer={timer}
				requestNewCodeHandler={requestNewCodeHandler}
				otpLength={OTP_LENGTH}
			/>

			<div className="border-t border-gray-200 mt-6 relative">
				<span className="text-gray-400 text-xs absolute left-[50%] top-[50%] translate-x-[-50%] translate-y-[-55%] bg-white px-2">
					or you can sign in with
				</span>
			</div>

			<Button
				variant="outline"
				className="gap-2 text-sm mt-6"
				onClick={() => navigate("/login?mode=email", { replace: true })}
			>
				<LuMail size={20} />
				<span>Continue with Email</span>
			</Button>

			<Button variant="outline" className="gap-2 text-sm mt-4">
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

export default MobileLoginFormContainer;
