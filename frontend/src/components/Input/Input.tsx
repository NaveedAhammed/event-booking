import clsx from "clsx";
import type { InputHTMLAttributes } from "react";
import type { FieldError, UseFormRegisterReturn } from "react-hook-form";

interface InputFieldProps extends InputHTMLAttributes<HTMLInputElement> {
	label?: string;
	error?: FieldError;
	registration: UseFormRegisterReturn;
}

const InputField = ({
	label,
	error,
	registration,
	className,
	...rest
}: InputFieldProps) => {
	return (
		<div className="space-y-1">
			{label && (
				<label className="block text-sm font-medium text-foreground">
					{label}
				</label>
			)}
			<input
				{...registration}
				{...rest}
				className={clsx(
					"w-full px-3 py-2 border rounded-md bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary",
					error && "border-red-500",
					className
				)}
			/>
			{error && (
				<p className="text-sm text-red-500 mt-1">{error.message}</p>
			)}
		</div>
	);
};

export default InputField;
