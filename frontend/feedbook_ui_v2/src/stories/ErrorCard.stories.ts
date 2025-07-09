import {Meta, StoryObj} from "@storybook/react";
import ErrorCard from "@/components/ErrorCard";
import {action} from "@storybook/addon-actions";

const meta = {
    title: "components/ErrorCard",
    component: ErrorCard,
} satisfies Meta<typeof ErrorCard>

export default meta

type Story = StoryObj<typeof ErrorCard>;

export const Default: Story = {
    args: {
        error: new Error("Error Message"),
        reset: () => action("reset")(),
    },
}
export const DevMode: Story = {
    args: {
        error: new Error("Error Message"),
        reset: () => action("reset")(),
        isDevMode: true
    },
}