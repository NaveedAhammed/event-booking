import api from "@/lib/axios";
import type { LoginRequest, RegisterRequest } from "../types/auth";
import type { AuthResponse } from "../types/response";

const authService = {
	async login(data: LoginRequest): Promise<AuthResponse> {
		const response = await api.post("/auth/login", data);
		return response.data;
	},

	async register(data: RegisterRequest): Promise<AuthResponse> {
		const response = await api.post("/auth/register", data);
		return response.data;
	},

	async oauthLogin(email: string, name: string): Promise<AuthResponse> {
		const response = await api.post("/auth/oauth", { email, name });
		return response.data;
	},

	async getProfile(): Promise<AuthResponse> {
		const response = await api.get("/auth/profile");
		return response.data;
	},
};

export default authService;
