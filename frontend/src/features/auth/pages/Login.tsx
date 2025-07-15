import { useSearchParams } from "react-router-dom";
import MobileLoginFormContainer from "../containers/MobileLoginFormContainer";
import EmailLoginFormContainer from "../containers/EmailLoginFormContainer";

const Login = () => {
	const [searchParams] = useSearchParams();

	const mode = searchParams.get("mode") ? searchParams.get("mode") : "email";

	return (
		<>
			{mode === "email" ? (
				<EmailLoginFormContainer />
			) : (
				<MobileLoginFormContainer />
			)}
		</>
	);
};

export default Login;
