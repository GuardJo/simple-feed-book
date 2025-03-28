import {Meta, StoryObj} from "@storybook/react";
import Spinner from "@/components/Spinner";

const meta = {
    title: "Components/Spinner",
    component: Spinner
} satisfies Meta<typeof Spinner>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {},
}
