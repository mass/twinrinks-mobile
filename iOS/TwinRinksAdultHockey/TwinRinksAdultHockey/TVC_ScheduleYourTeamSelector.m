#import "TVC_ScheduleYourTeamSelector.h"
#import "MemoryManager.h"
#import "Team.h"
#import "VC_ScheduleGames.h"

@interface TVC_ScheduleYourTeamSelector ()

@property (strong, nonatomic) NSArray *yourTeamsArray;

@end

@implementation TVC_ScheduleYourTeamSelector

-(id)initWithStyle:(UITableViewStyle)style {
    self = [super initWithStyle:style];
    if (self) {}
    return self;
}

-(void)viewDidLoad {
    [super viewDidLoad];

    MemoryManager *myManger = [[MemoryManager alloc]init];
    _yourTeamsArray = [myManger getYourTeamArray];

}

#pragma mark - Table view data source

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _yourTeamsArray.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *cellIdentifier = @"id_cell_scheduleYourTeamSelector";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier forIndexPath:indexPath];
    
    if (cell == nil)
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    
    Team *temp = [_yourTeamsArray objectAtIndex:indexPath.row];
    ((UILabel *)[cell viewWithTag:0]).text = [temp toString];
    
    return cell;
}

#pragma mark - Table view delegate

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    VC_ScheduleGames *vc_ScheduleGames = [self.storyboard instantiateViewControllerWithIdentifier:@"id_vc_scheduleGames"];
    vc_ScheduleGames.dataToDisplay = [((Team *) [_yourTeamsArray objectAtIndex:indexPath.row]) getTeamKey];
    [self.navigationController pushViewController:vc_ScheduleGames animated:YES];
}

@end