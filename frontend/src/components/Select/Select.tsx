import clsx from "clsx";
import type { FieldError } from "react-hook-form";

interface Option {
	label: string;
	value: string | number;
}

interface SelectProps {
	label?: string;
	name: string;
	options: Option[];
	value?: string | number;
	onChange: (value: string) => void;
	error?: FieldError;
	disabled?: boolean;
	className?: string;
}

export const Select = ({
	label,
	name,
	options,
	value,
	onChange,
	error,
	disabled,
	className,
}: SelectProps) => {
	return (
		<div className="w-full">
			{label && (
				<label
					htmlFor={name}
					className="block text-sm font-medium text-foreground mb-1"
				>
					{label}
				</label>
			)}
			<select
				name={name}
				id={name}
				value={value}
				onChange={(e) => onChange(e.target.value)}
				disabled={disabled}
				className={clsx(
					"w-full px-3 py-2 border rounded-md text-sm bg-background text-foreground",
					"focus:outline-none focus:ring-2 focus:ring-ring",
					error && "border-red-500",
					disabled && "opacity-50 cursor-not-allowed",
					className
				)}
			>
				<option value="" disabled>
					Select {label || name}
				</option>
				{options.map((opt) => (
					<option key={opt.value} value={opt.value}>
						{opt.label}
					</option>
				))}
			</select>
			{error && <p className="text-sm text-red-500 mt-1">{error.message}</p>}
		</div>
	);
};
