import { Outlet } from "react-router-dom";
import NavBar from "./NavBar";
import Footer from "./Footer";

function AppLayout() {
	return (
		<div>
			<NavBar />
			<main className="max-w-[1200px] mx-auto min-h-[85vh] h-full bg-amber-500">
				<Outlet />
			</main>
			<Footer />
		</div>
	);
}

export default AppLayout;
