import clsx from "clsx";
import type { ButtonHTMLAttributes, ReactNode } from "react";

type Variant = "primary" | "secondary" | "danger" | "outline" | "ghost";

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
	children: ReactNode;
	variant?: Variant;
	isLoading?: boolean;
}

const Button = ({
	children,
	variant = "primary",
	isLoading = false,
	className,
	disabled,
	...props
}: ButtonProps) => {
	const baseStyles =
		"inline-flex items-center justify-center px-4 py-2 rounded-md font-medium focus:outline-none transition cursor-pointer";

	const varientStyles: Record<Variant, string> = {
		primary: "bg-primary text-primary-foreground hover:opacity-90",
		secondary: "bg-secondary text-secondary-foreground hover:bg-accent",
		danger: "bg-destructive text-white hover:opacity-90",
		outline: "border text-foreground hover:bg-muted",
		ghost: "bg-transparent hover:bg-muted",
	};

	return (
		<button
			className={clsx(
				baseStyles,
				varientStyles[variant],
				className,
				disabled && "opacity-50 cursor-not-allowed"
			)}
			disabled={disabled || isLoading}
			{...props}
		>
			{isLoading ? "isLoading..." : children}
		</button>
	);
};

export default Button;
