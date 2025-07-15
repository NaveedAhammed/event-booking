import { Link, NavLink } from "react-router-dom";

import logo from "@/assets/logo.png";
import LinkButton from "@/components/Button/LinkButton";

function NavBar() {
	return (
		<header className="h-16 flex items-center shadow">
			<div className="w-[1200px] mx-auto flex items-center justify-between">
				<Link to="/">
					<div className="h-12 w-12 flex items-center">
						<img
							src={logo}
							alt="Event Booking"
							className="h-full w-full object-cover"
						/>
						<span className="text-2xl font-bold">Eventer</span>
					</div>
				</Link>
				<nav>
					<ul className="flex items-center gap-8">
						<li>
							<NavLink to="/">Home</NavLink>
						</li>
						<li>
							<NavLink to="/login?mode=email">Login</NavLink>
						</li>
						<li>
							<LinkButton to="/register">Sign Up</LinkButton>
						</li>
					</ul>
				</nav>
			</div>
		</header>
	);
}

export default NavBar;
