export interface Activity {
    id: number;
    name: string;
    maxScore: number;
}

export interface StudentScore {
    id: number;
    username: string;
    firstname: string;
    lastname: string;
    value: number | null;
}

export interface ActivityScore {
    activity: Activity;
    values: number | null;
}

export interface ActivityScoreList {
    activity: Activity;
    students: StudentScore[];
}