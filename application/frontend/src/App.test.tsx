import React from 'react';
import { screen, waitFor } from '@testing-library/react';
import { shallow, mount, render } from 'enzyme';
import App from './App';
import NavBar from './components/NavBar'
import Sidebar from './components/Sidebar';
import { Menu } from "antd";


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

