import { Link } from "react-router-dom";
import clsx from "clsx";
import type { ReactNode } from "react";

type Variant = "primary" | "secondary" | "danger" | "outline" | "ghost";

type LinkButtonProps = {
	to: string;
	variant?: Variant;
	children: ReactNode;
	className?: string;
};

const LinkButton = ({
	to,
	variant = "primary",
	children,
	className,
}: LinkButtonProps) => {
	const baseStyles =
		"inline-flex items-center justify-center px-4 py-2 rounded-md font-medium focus:outline-none transition";

	const varientStyles: Record<Variant, string> = {
		primary: "bg-primary text-primary-foreground hover:opacity-90",
		secondary: "bg-secondary text-secondary-foreground hover:bg-accent",
		danger: "bg-destructive text-white hover:opacity-90",
		outline: "border text-foreground hover:bg-muted",
		ghost: "bg-transparent hover:bg-muted",
	};

	return (
		<Link
			to={to}
			className={clsx(baseStyles, varientStyles[variant], className)}
		>
			{children}
		</Link>
	);
};

export default LinkButton;
