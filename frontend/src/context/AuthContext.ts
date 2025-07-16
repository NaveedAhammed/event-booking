import { createContext } from "react";

interface User {
	id: string;
	name: string;
	email: string;
	role: "USER" | "ORGANIZER" | "ADMIN";
}

export interface AuthState {
	isAuthenticated: boolean;
	accessToken: string | null;
	user: User | null;
}

const AuthContext = createContext<AuthState | null>(null);

export default AuthContext;
