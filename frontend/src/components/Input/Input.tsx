import clsx from "clsx";
import type { InputHTMLAttributes } from "react";
import type { FieldError, UseFormRegisterReturn } from "react-hook-form";
import type { IconType } from "react-icons";

interface InputFieldProps extends InputHTMLAttributes<HTMLInputElement> {
	leadingIcon?: IconType;
	leadingIconSize?: number;
	tralingIcon?: IconType;
	tralingIconSize?: number;
	label?: string;
	error?: FieldError;
	registration: UseFormRegisterReturn;
}

const InputField = ({
	leadingIcon: LeadingIcon,
	leadingIconSize = 20,
	tralingIcon: TralingIcon,
	tralingIconSize = 20,
	error,
	registration,
	className,
	...rest
}: InputFieldProps) => {
	const baseStyle =
		"flex-1 outline-none placeholder:text-gray-500 placeholder:text-sm placeholder:pl-2";

	return (
		<div className="w-full">
			<div className="w-full h-10 flex items-center ring ring-gray-300 focus-within:ring-primary rounded-xl">
				{LeadingIcon && (
					<div className="w-10 h-10 flex items-center justify-center text-gray-500">
						<LeadingIcon size={leadingIconSize} />
					</div>
				)}
				<input
					className={clsx(baseStyle, className)}
					{...registration}
					{...rest}
				/>
				{TralingIcon && (
					<div className="w-10 h-10 flex items-center justify-center text-gray-500 cursor-pointer">
						<TralingIcon size={tralingIconSize} />
					</div>
				)}
			</div>
			{error && (
				<p className="text-xs ml-3 mt-2 font-semibold text-rose-500">
					{error.message}
				</p>
			)}
		</div>
	);
};

export default InputField;
