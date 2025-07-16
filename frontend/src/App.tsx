import { RouterProvider } from "react-router-dom";
import { router } from "./routes/AppRoutes";
import { Toaster } from "react-hot-toast";
import AuthProvider from "./providers/AuthProvider";

const App = () => {
	return (
		<AuthProvider>
			<Toaster
				position="bottom-center"
				toastOptions={{
					style: {
						maxWidth: "50vw",
					},
				}}
			/>
			<RouterProvider router={router} />
		</AuthProvider>
	);
};

export default App;
