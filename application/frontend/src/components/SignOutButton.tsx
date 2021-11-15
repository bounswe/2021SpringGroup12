import { Button } from "antd";
import { PoweroffOutlined } from "@ant-design/icons";
import {useHistory} from "react-router";

export interface ISignOutButtonProps {}

export function SignOutButton(props: ISignOutButtonProps) {
  const history = useHistory()
  const  onClick = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    history.push("/")
    window.location.reload();
  };
  return (
    <>
      <Button type="primary" icon={<PoweroffOutlined />} onClick={onClick}>
        Sign Out
      </Button>
    </>
  );
}
