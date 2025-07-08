import { createBrowserRouter } from "react-router-dom";

import AppLayout from "@/layouts/AppLayout";
import Register from "@/features/auth/pages/Register";
import Login from "@/features/auth/pages/Login";
import Home from "@/features/home/pages/Home";

export const router = createBrowserRouter([
	{
		path: "/",
		element: <AppLayout />,
		children: [
			{
				index: true,
				element: <Home />,
			},
			{
				path: "register",
				element: <Register />,
			},
			{
				path: "login",
				element: <Login />,
			},
		],
	},
]);
