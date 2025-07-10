import clsx from "clsx";
import type { InputHTMLAttributes } from "react";
import type { FieldError, UseFormRegisterReturn } from "react-hook-form";
import type { IconType } from "react-icons";

interface InputFieldProps extends InputHTMLAttributes<HTMLInputElement> {
	leadingIcon?: IconType;
	leadingIconSize?: number;
	trailingIcon?: IconType;
	trailingIconSize?: number;
	error?: FieldError;
	registration: UseFormRegisterReturn;
	disabled?: boolean;
}

const InputField = ({
	leadingIcon: LeadingIcon,
	leadingIconSize = 20,
	trailingIcon: TrailingIcon,
	trailingIconSize = 20,
	error,
	registration,
	className,
	disabled,
	...rest
}: InputFieldProps) => {
	const baseStyle =
		"flex-1 outline-none placeholder:text-gray-400 placeholder:text-sm placeholder:pl-2";

	return (
		<div
			className={`w-full ${disabled ? "opacity-50 pointer-events-none" : ""}`}
		>
			<div
				className={`w-full h-10 flex items-center ring ${
					error
						? "ring-rose-500 focus-within:ring-destructive"
						: "ring-gray-300 focus-within:ring-primary"
				} rounded-xl`}
			>
				{LeadingIcon && (
					<div className="w-10 h-10 flex items-center justify-center text-gray-400">
						<LeadingIcon size={leadingIconSize} />
					</div>
				)}
				<input
					className={clsx(baseStyle, className)}
					{...registration}
					{...rest}
					disabled={disabled}
				/>
				{TrailingIcon && (
					<div className="w-10 h-10 flex items-center justify-center text-gray-400 cursor-pointer">
						<TrailingIcon size={trailingIconSize} />
					</div>
				)}
			</div>
			{error && (
				<p className="text-xs ml-3 mt-2 font-semibold text-destructive">
					{error.message}
				</p>
			)}
		</div>
	);
};

export default InputField;
