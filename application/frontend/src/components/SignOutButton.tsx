import { Button } from "antd";
import { PoweroffOutlined } from "@ant-design/icons";

export interface ISignOutButtonProps {}

export function SignOutButton(props: ISignOutButtonProps) {
  const  onClick = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
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
