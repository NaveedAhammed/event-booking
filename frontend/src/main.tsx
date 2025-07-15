import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./styles/index.css";
import App from "./App.tsx";
import { Toaster } from "react-hot-toast";

createRoot(document.getElementById("root")!).render(
	<StrictMode>
		<Toaster
			position="bottom-center"
			toastOptions={{
				style: {
					maxWidth: "50vw",
				},
			}}
		/>
		<App />
	</StrictMode>
);
