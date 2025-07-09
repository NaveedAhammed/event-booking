import { z } from "zod";

export const registerSchema = z.object({
	name: z
		.string()
		.nonempty({ message: "Name is required" })
		.min(5, { message: "Name should be at least 5 characters" }),
	email: z
		.string()
		.nonempty({ message: "Email is required" })
		.email({ message: "Invalid email address" }),
	password: z.string().nonempty({ message: "Password is required" }),
	role: z.string(),
});

export type RegisterSchema = z.infer<typeof registerSchema>;
