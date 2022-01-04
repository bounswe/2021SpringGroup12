import React from 'react';
import { screen, waitFor } from '@testing-library/react';
import { shallow, mount, render } from 'enzyme';
import App from './App';
import NavBar from './components/NavBar'
import Sidebar from './components/Sidebar';
import { Menu } from "antd";
import { SignOutButton } from "./components/SignOutButton";
import { LoginForm } from "./components/LoginForm";
import { Login } from "./pages/Login";

test('Check navbar component is displayed', async () => {
  const wrapper = mount(<App/>)
  await waitFor(()=>{})
  expect(wrapper.find(NavBar)).toBeTruthy()
});


test('sidebar has no element when there is no logged in user', async () => {
  const wrapper = mount(<App/>)
  await waitFor(()=>{})
  expect(wrapper.find(Sidebar).contains("Goals")).toBeFalsy()
  expect(wrapper.find(Sidebar).contains("Dashboard")).toBeFalsy()
});

//batu

test('Navigation bar has not Feed when there is no logged in user', async () => {
  const wrapper = mount(<App/>)
  await waitFor(()=>{})
  expect(wrapper.find(NavBar).contains("Feed")).toBeFalsy()
});

test('Navigation bar has Login when there is no logged in user', async () => {
  const wrapper = mount(<App/>)
  await waitFor(()=>{})
  expect(wrapper.find(NavBar).contains("Login")).toBeTruthy()
});

test('Navigation bar has Register when there is no logged in user', async () => {
  const wrapper = mount(<App/>)
  await waitFor(()=>{})
  expect(wrapper.find(NavBar).contains("Register")).toBeTruthy()
});


/*
test('Check signout button is displayed', async () => {
  const wrapper = mount(<NavBar user="batu"/>)
  await waitFor(()=>{})
  expect(wrapper.find(Home)).toBeTruthy()
});


test('Check username text is displayed on login page', async () => {
  const wrapper = mount(<Login/>)
  await waitFor(()=>{})
  expect(wrapper.find(LoginForm).contains("Username")).toBeTruthy()
});

test('Check login text is displayed on login page', async () => {
  const wrapper = mount(<Login/>)
  await waitFor(()=>{})
  expect(wrapper.find(LoginForm).contains("Password")).toBeTruthy()
});
*/
