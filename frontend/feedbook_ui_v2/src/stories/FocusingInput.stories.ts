import {Meta, StoryObj} from "@storybook/react";
import FocusingInput from "@/components/FocusingInput";
import {fn} from "@storybook/test";

const meta = {
    title: "components/FocusingInput",
    component: FocusingInput,
    parameters: {
        layout: "centered"
    }
} satisfies Meta<typeof FocusingInput>

export default meta

type Story = StoryObj<typeof meta>

export const Username: Story = {
    args: {
        id: 'username',
        labelName: 'ID',
        type: "text",
        onChange: fn(),
    }
}

export const Nickname: Story = {
    args: {
        id: 'nickname',
        labelName: 'Name',
        type: "text",
        onChange: fn(),
    }
}

export const Password: Story = {
    args: {
        id: 'password',
        labelName: 'Password',
        type: "password",
        onChange: fn(),
    }
}