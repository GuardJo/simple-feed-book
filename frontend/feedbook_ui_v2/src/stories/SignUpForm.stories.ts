import {Meta, StoryObj} from "@storybook/react";
import SignUpForm from "@/components/SignUpForm";

const meta = {
    title: "components/SignUpForm",
    component: SignUpForm,
} satisfies Meta<typeof SignUpForm>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {}
}