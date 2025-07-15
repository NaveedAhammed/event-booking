import api from "@/lib/axios";
import type {
	LoginRequest,
	OtpVerifyRequest,
	RegisterRequest,
} from "../types/auth";

const authService = {
	async login(data: LoginRequest): Promise<string> {
		const response = await api.post("/auth/login", data);
		return response.data.access_token;
	},

	async register(data: RegisterRequest): Promise<string> {
		const response = await api.post("/auth/register", data);
		return response.data.access_token;
	},

	async oauthLogin(email: string, name: string): Promise<string> {
		const response = await api.post("/auth/oauth", { email, name });
		return response.data.access_token;
	},

	async getOtp(mobile: string): Promise<boolean> {
		const response = await api.post("/auth/send-otp", { mobile });
		return response.status === 200;
	},

	async verifyOtp(data: OtpVerifyRequest): Promise<string> {
		const response = await api.post("/auth/verify-otp", data);
		return response.data.access_token;
	},
};

export default authService;
