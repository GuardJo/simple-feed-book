import {Meta, StoryObj} from "@storybook/react";
import FeedCreateForm from "@/components/FeedCreateForm";

const meta = {
    title: "Components/FeedCreateForm",
    component: FeedCreateForm,
} satisfies Meta<typeof FeedCreateForm>

export default meta;

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {},
}