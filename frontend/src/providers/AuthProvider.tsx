import AuthContext, { AuthState } from "@/context/AuthContext";
import { useState, type ReactNode } from "react";

const AuthProvider = ({ children }: { children: ReactNode }) => {
	const [authState, setAuthState] = useState<AuthState | null>(null);

	return (
		<AuthContext.Provider
			value={{
				isAuthenticated: false,
				accessToken: null,
				user: null,
			}}
		>
			{children}
		</AuthContext.Provider>
	);
};

export default AuthProvider;
