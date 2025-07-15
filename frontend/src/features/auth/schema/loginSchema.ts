import { z } from "zod";

export const emailLoginSchema = z.object({
	email: z
		.string()
		.nonempty({ message: "Email is required" })
		.email({ message: "Invalid email address" }),
	password: z.string().nonempty({ message: "Password is required" }),
});

export type EmailLoginSchema = z.infer<typeof emailLoginSchema>;

export const mobileLoginSchema = z.object({
	mobile: z.string().nonempty({ message: "Mobile number is required" }),
	otp: z.string().optional(),
});

export type MobileLoginSchema = z.infer<typeof mobileLoginSchema>;
