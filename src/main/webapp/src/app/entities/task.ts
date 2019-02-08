import { TaskStatus } from './task-status.enum';

export class Task {
    constructor(
        public id?: number,
        public description?: string,
        public status?: TaskStatus,
        public pictureContentType?: string,
        public picture?: any
    ) {}
}
