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
	mobile: string;
	otp: string;
	status: "idle" | "loading" | "error";
	showOtpComponent: boolean;
	timer: number;
	errorMessage?: string;
}

const initialState: State = {
	mobile: "",
	otp: "",
	status: "idle",
	showOtpComponent: false,
	timer: 60,
	errorMessage: undefined,
};

const reducer = (state: State, action: Action): State => {
	switch (action.type) {
		case "sendOtpStart":
			return { ...state, status: "loading" };
		case "sendOtpSuccess":
			return {
				...state,
				status: "idle",
				showOtpComponent: true,
				timer: 60,
			};
		case "sendOtpError":
			return { ...state, status: "error", errorMessage: action.payload };
		case "otpVerifyStart":
			return { ...state, status: "loading" };
		case "otpVerifySuccess":
			return {
				...state,
				status: "idle",
			};
		case "otpVerifyError":
			return { ...state, status: "error", errorMessage: action.payload };
		case "decrementTimer":
			return { ...state, timer: state.timer - 1 };
		case "updateMobile":
			return { ...state, mobile: action.payload };
		case "updateOtp":
			return { ...state, otp: action.payload };
		case "resendCode":
			return { ...state, timer: 60 };
		case "reset":
			return initialState;
		default:
			return state;
	}
};

const MobileLoginFormContainer = () => {
	const [state, dispatch] = useReducer(reducer, initialState);

	const { showOtpComponent, timer, mobile, otp } = state;

	const {
		handleSubmit,
		register,
		formState: { errors, isSubmitting },
	} = useForm<MobileLoginSchema>({
		resolver: zodResolver(mobileLoginSchema),
	});

	const onSubmit = async (data: MobileLoginSchema) => {
		if (!showOtpComponent) return getOtp(data.mobile!);
		else if (showOtpComponent) return verifyOtp({ mobile, otp });
	};

	const getOtp = async (mobile: string) => {
		dispatch({ type: "sendOtpStart" });
		try {
			await authService.getOtp(mobile);
			dispatch({ type: "sendOtpSuccess" });
		} catch (err) {
			console.log(err);
			dispatch({ type: "sendOtpError", payload: "Failed to send OTP" });
		}
	};

	const verifyOtp = async (data: MobileLoginSchema) => {
		dispatch({ type: "otpVerifyStart" });
		try {
			await authService.verifyOtp(data);
			dispatch({ type: "otpVerifySuccess" });
		} catch (err) {
			console.log(err);
			dispatch({ type: "otpVerifyError", payload: "Failed to send OTP" });
		}
	};

	const requestNewCodeHandler = () => {
		dispatch({ type: "resendCode" });
	};

	const otpUpdateHandler = (otp: string) => {
		console.log(otp);
		dispatch({ type: "updateOtp", payload: otp });
	};

	const mobileUpdateHandler = (mobile: string) => {
		console.log(mobile);
		dispatch({ type: "updateMobile", payload: mobile });
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
				onSubmit={handleSubmit(onSubmit, (e) => console.log(e))}
				showOtpComponent={showOtpComponent}
				timer={timer}
				requestNewCodeHandler={requestNewCodeHandler}
				otpUpdateHandler={otpUpdateHandler}
				mobileUpdateHandler={mobileUpdateHandler}
			/>

			<div className="border-t border-gray-200 mt-6 relative">
				<span className="text-gray-400 text-xs absolute left-[50%] top-[50%] translate-x-[-50%] translate-y-[-55%] bg-white px-2">
					or you can sign in with
				</span>
			</div>

			<Button variant="outline" className="gap-2 text-sm mt-6">
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
