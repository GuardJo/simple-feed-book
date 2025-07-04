import {Meta, StoryObj} from "@storybook/react";
import NotificationScrollList from "@/components/NotificationScrollList";

const meta = {
    title: 'Components/NotificationScrollList',
    component: NotificationScrollList,
} satisfies Meta<typeof NotificationScrollList>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {},
}
