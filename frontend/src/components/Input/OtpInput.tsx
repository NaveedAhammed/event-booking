import { useRef, type ChangeEvent, type KeyboardEvent } from "react";
import Button from "../Button/Button";

interface Props {
	length?: number;
	timer?: number;
	requestNewCodeHandler: () => void;
	onChange?: (otp: string) => void;
}

function OtpInput({
	length = 6,
	timer,
	requestNewCodeHandler,
	onChange,
}: Props) {
	const inputsRef = useRef<HTMLInputElement[]>([]);

	const focusInput = (index: number) => {
		inputsRef.current[index]?.focus();
	};

	const handleOnChange = (e: ChangeEvent<HTMLInputElement>, i: number) => {
		const value = e.target.value.replace(/\D/g, "").slice(-1);
		e.target.value = value;

		if (value && i < length - 1) {
			focusInput(i + 1);
		}
		triggerOtpUpdate();
	};

	const handleKeyDown = (e: KeyboardEvent<HTMLInputElement>, i: number) => {
		const input = e.currentTarget;

		if (e.key === "Backspace") {
			if (input.value === "" && i > 0) {
				focusInput(i - 1);
			} else {
				input.value = "";
			}
			e.preventDefault();
			triggerOtpUpdate();
		}

		if (e.key === "ArrowLeft" && i > 0) {
			focusInput(i - 1);
		} else if (e.key === "ArrowRight" && i < length - 1) {
			focusInput(i + 1);
		}
	};

	const triggerOtpUpdate = () => {
		const otp = inputsRef.current
			.map((input) => input?.value || "")
			.join("");
		onChange?.(otp);
	};

	return (
		<div className="flex flex-col py-2 gap-4">
			<div className="flex items-center justify-between">
				{Array.from({ length }).map((_, i) => (
					<input
						min={0}
						max={9}
						type="number"
						key={i}
						onChange={(e) => handleOnChange(e, i)}
						onKeyDown={(e) => handleKeyDown(e, i)}
						className="outline-none ring ring-gray-300 focus-within:ring-primary rounded-md p-4 text-center"
						ref={(ele) => {
							inputsRef.current[i] = ele!;
						}}
					/>
				))}
			</div>
			<div className="flex flex-col gap-2">
				{timer && timer > 0 ? (
					<span className="text-xs text-gray-500 text-center">
						Didn't get an email? Check your spam folder or request
						another code in {timer} seconds
					</span>
				) : (
					<Button
						variant="ghost"
						className="text-sm text-primary"
						onClick={requestNewCodeHandler}
					>
						Request new code
					</Button>
				)}
			</div>
		</div>
	);
}

export default OtpInput;
