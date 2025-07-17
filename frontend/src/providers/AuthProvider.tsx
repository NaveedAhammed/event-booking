import AuthContext, { type User } from "@/context/AuthContext";
import userService from "@/features/account/service/userService";
import api from "@/lib/axios";
import { useEffect, useLayoutEffect, useState, type ReactNode } from "react";

const AuthProvider = ({ children }: { children: ReactNode }) => {
	const [user, setUser] = useState<User | null>(null);
	const [accessToken, setAccessToken] = useState<string | null>(null);

	useEffect(() => {
		const fetchMe = async () => {
			try {
				const response = await userService.getMe();
				setUser(response);
			} catch {
				setUser(null);
			}
		};

		fetchMe();
	}, []);

	useLayoutEffect(() => {
		const authInterceptor = api.interceptors.request.use((config) => {
			config.headers.Authorization =
				!config._retry && accessToken
					? `Bearer ${accessToken}`
					: config.headers.Authorization;
			return config;
		});

		return () => {
			api.interceptors.request.eject(authInterceptor);
		};
	}, [accessToken]);

	useLayoutEffect(() => {
		const refreshInterceptor = api.interceptors.response.use(
			(response) => response,
			async (error) => {
				const originalRequest = error.config;

				if (
					error.response.status === 403 &&
					error.response.data.errorCode === "INVALID_ACCESS_TOKEN"
				) {
					try {
						const response = await api.post("/auth/refresh");
						setAccessToken(response.data.access_token);

						originalRequest.headers.Authorization = `Bearer ${response.data.access_token}`;
						originalRequest._retry = true;

						return api(originalRequest);
					} catch {
						setAccessToken(null);
					}
				}

				return Promise.reject(error);
			}
		);

		return () => {
			api.interceptors.response.eject(refreshInterceptor);
		};
	}, []);

	return (
		<AuthContext.Provider
			value={{
				isAuthenticated: accessToken ? true : false,
				accessToken: accessToken || null,
				user: user || null,
				setAccessToken,
				setUser,
			}}
		>
			{children}
		</AuthContext.Provider>
	);
};

export default AuthProvider;
