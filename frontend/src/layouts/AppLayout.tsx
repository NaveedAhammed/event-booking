import { Outlet } from "react-router-dom";
import NavBar from "./NavBar";
import Footer from "./Footer";

function AppLayout() {
	return (
		<div>
			<NavBar />
			<main className="max-w-[1200px] mx-auto flex items-center justify-center">
				<Outlet />
			</main>
			<Footer />
		</div>
	);
}

export default AppLayout;
