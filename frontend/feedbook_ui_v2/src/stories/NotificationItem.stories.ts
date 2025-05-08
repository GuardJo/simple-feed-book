import {Meta, StoryObj} from "@storybook/react";
import NotificationItem from "@/components/NotificationItem";

const meta = {
    title: "Components/NotificationItem",
    component: NotificationItem
} satisfies Meta<typeof NotificationItem>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        notification: {
            alarmText: 'Test 알림',
            alarmTime: '2시간',
        }
    }
}