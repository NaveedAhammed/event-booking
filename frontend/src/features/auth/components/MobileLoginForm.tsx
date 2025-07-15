import Heading from "@/components/Heading/Heading";
import InputField from "@/components/Input/Input";
import type { FieldErrors, UseFormRegister } from "react-hook-form";
import type { MobileLoginSchema } from "../schema/loginSchema";
import { MdOutlinePhoneIphone } from "react-icons/md";
import Button from "@/components/Button/Button";
import OtpInput from "@/components/Input/OtpInput";

interface Props {
	onSubmit: () => void;
	register: UseFormRegister<MobileLoginSchema>;
	errors: FieldErrors<MobileLoginSchema>;
	isSubmitting: boolean;
	showOtpComponent: boolean;
	timer?: number;
	requestNewCodeHandler: () => void;
	otpLength: number;
}

function MobileLoginForm({
	onSubmit,
	register,
	errors,
	isSubmitting,
	showOtpComponent,
	timer,
	requestNewCodeHandler,
	otpLength,
}: Props) {
	return (
		<form className="bg-white w-96 flex flex-col gap-4" onSubmit={onSubmit}>
			<Heading
				title="Welcome Back!"
				subTitle="Login to your account to continue booking events and managing your
				favorites."
			/>

			<InputField
				registration={register("mobile")}
				type="number"
				placeholder="Mobile number"
				leadingIcon={MdOutlinePhoneIphone}
				error={errors.mobile}
				disabled={isSubmitting}
			/>

			{showOtpComponent && (
				<OtpInput
					requestNewCodeHandler={requestNewCodeHandler}
					length={otpLength}
					timer={timer}
					registration={register("otp")}
					error={errors.otp}
					disabled={isSubmitting}
				/>
			)}

			<Button type="submit" disabled={isSubmitting}>
				{showOtpComponent ? "Submit" : "Login"}
			</Button>
		</form>
	);
}

export default MobileLoginForm;
