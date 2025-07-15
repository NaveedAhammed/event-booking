import clsx from "clsx";

interface Props {
	title: string;
	subTitle?: string;
	className?: string;
}

function Heading({ className, title, subTitle }: Props) {
	return (
		<div className={clsx(className, "flex flex-col gap-2")}>
			<h2 className="text-2xl font-semibold">{title}</h2>
			{subTitle && (
				<p className="text-sm text-gray-500 mb-4">{subTitle}</p>
			)}
		</div>
	);
}

export default Heading;
