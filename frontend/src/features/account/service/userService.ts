import api from "@/lib/axios";
import type { UserResponse } from "../types/user";

const userService = {
	async getMe(): Promise<UserResponse> {
		const response = await api.get("/users/me");
		return response.data;
	},
};

export default userService;
