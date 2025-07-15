import axios from "axios";

export const getErrorMessage = (error: unknown) => {
	if (axios.isAxiosError(error)) {
		if (error.response?.data) {
			const data = error.response.data as {
				message?: string;
				errorCode?: string;
			};
			return data.message ?? "Unexpected server error";
		} else if (error.message) {
			return error.message;
		}
	}

	return "Something went wrong.";
};
