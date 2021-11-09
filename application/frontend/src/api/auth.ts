import axios from "axios";
import { IregisterForm } from "../components/RegistrationForm";

export const registerRequest = (values:IregisterForm) => {
  axios
    .post("/registerURL", {
      email: values.email,
      password: values.password,
      username: values.username,
    })
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });
};

export const loginRequest = (values:IregisterForm) => {
  axios
    .post("/loginURL", {
      email: values.email,
      password: values.password,
      username: values.username,
    })
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });
};