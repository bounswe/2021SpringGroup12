import axios from "axios";
import { IregisterForm } from "../components/RegistrationForm";

// TODO use this api calls later
export const registerRequest = (values: IregisterForm) => {
  axios
    .post("/signup", {
      email: values.email,
      password: values.password,
      username: values.username,
    })
    .then((res) => {
      console.log(res);
    })
    .catch((err) => {
      console.log(err);
    });
};

export const loginRequest = (values: IregisterForm): any => {
  axios
    .post("/login", {
      email: values.email,
      password: values.password,
      username: values.username,
    })
    .then((res) => {
      console.log(res.data);
      return res;
    })
    .catch((error) => {
      console.log(error);
      return error;
    });
};
