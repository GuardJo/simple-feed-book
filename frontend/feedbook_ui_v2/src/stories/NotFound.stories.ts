import {Meta, StoryObj} from "@storybook/react";
import NotFoundCard from "@/components/NotFoundCard";

const meta = {
    title: "components/NotFoundCard",
    component: NotFoundCard
} satisfies Meta<typeof NotFoundCard>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {},
}